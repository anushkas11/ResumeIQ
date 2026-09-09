import { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import { analysisApi, optimizeApi, downloadBlob } from '../api/resources'
import Navbar from '../components/Navbar'
import ScoreDial from '../components/ScoreDial'

export default function AnalysisPage() {
  const { id } = useParams()

  const [analysis, setAnalysis] = useState(null)
  const [loading, setLoading] = useState(true)
  const [loadError, setLoadError] = useState('')

  const [confirmed, setConfirmed] = useState({})
  const [optimizing, setOptimizing] = useState(false)
  const [optimizeError, setOptimizeError] = useState('')
  const [optimized, setOptimized] = useState(null)

  const [exporting, setExporting] = useState('')

  useEffect(() => {
    analysisApi
      .getById(id)
      .then((res) => setAnalysis(res.data))
      .catch(() => setLoadError('Could not load this analysis.'))
      .finally(() => setLoading(false))
  }, [id])

  const toggleConfirmed = (item) => {
    setConfirmed((prev) => ({ ...prev, [item]: !prev[item] }))
  }

  const handleOptimize = async () => {
    setOptimizing(true)
    setOptimizeError('')
    try {
      const confirmedAdditions = Object.keys(confirmed).filter((k) => confirmed[k])
      const res = await optimizeApi.optimize({ analysisId: Number(id), confirmedAdditions })
      setOptimized(res.data)
    } catch (err) {
      setOptimizeError(err.response?.data?.message || 'Could not generate the optimized resume.')
    } finally {
      setOptimizing(false)
    }
  }

  const handleExport = async (format) => {
    if (!optimized) return
    setExporting(format)
    try {
      const res = await optimizeApi.exportFile(optimized.id, format)
      downloadBlob(res.data, `optimized-resume.${format}`)
    } catch {
      setOptimizeError('Export failed. Please try again.')
    } finally {
      setExporting('')
    }
  }

  if (loading) {
    return (
      <div>
        <Navbar />
        <main className="analysis-page">Loading analysis…</main>
      </div>
    )
  }

  if (loadError || !analysis) {
    return (
      <div>
        <Navbar />
        <main className="analysis-page">
          <div className="error-banner">{loadError || 'Analysis not found.'}</div>
          <Link to="/dashboard">Back to dashboard</Link>
        </main>
      </div>
    )
  }

  return (
    <div>
      <Navbar />

      <main className="analysis-page">
        <Link to="/dashboard" className="back-link">
          ← Back to dashboard
        </Link>

        <h1>Gap analysis</h1>

        <section className="panel scores-panel">
          <ScoreDial label="Overall ATS score" score={analysis.overallAtsScore} size={120} />
          <div className="scores-panel__breakdown">
            <ScoreDial label="Keyword match" score={analysis.keywordMatchScore} />
            <ScoreDial label="Formatting" score={analysis.formattingScore} />
            <ScoreDial label="Skills coverage" score={analysis.skillsCoverageScore} />
            <ScoreDial label="Experience fit" score={analysis.experienceRelevanceScore} />
          </div>
        </section>

        {analysis.summary && (
          <section className="panel text-panel">
            <h2>Summary</h2>
            <p>{analysis.summary}</p>
          </section>
        )}

        <div className="two-col">
          <section className="panel text-panel">
            <h2>Missing keywords</h2>
            {analysis.missingKeywords?.length ? (
              <div className="tag-list">
                {analysis.missingKeywords.map((kw) => (
                  <span className="tag tag--gap" key={kw}>
                    {kw}
                  </span>
                ))}
              </div>
            ) : (
              <p className="muted">None found — good coverage.</p>
            )}
          </section>

          <section className="panel text-panel">
            <h2>Weak bullet points</h2>
            {analysis.weakBullets?.length ? (
              <ul className="bullet-list">
                {analysis.weakBullets.map((b, i) => (
                  <li key={i}>{b}</li>
                ))}
              </ul>
            ) : (
              <p className="muted">Nothing flagged.</p>
            )}
          </section>
        </div>

        <section className="panel text-panel">
          <h2>Confirm any that apply to you</h2>
          <p className="muted">
            These are skills the job description mentions that don't appear in your resume text.
            Only check the ones you genuinely have — nothing else will be added to your rewritten resume.
          </p>
          {analysis.suggestedAdditions?.length ? (
            <ul className="confirm-list">
              {analysis.suggestedAdditions.map((item) => (
                <li key={item}>
                  <label>
                    <input
                      type="checkbox"
                      checked={Boolean(confirmed[item])}
                      onChange={() => toggleConfirmed(item)}
                    />
                    <span>{item}</span>
                  </label>
                </li>
              ))}
            </ul>
          ) : (
            <p className="muted">No suggestions this time.</p>
          )}

          {optimizeError && <div className="error-banner">{optimizeError}</div>}

          <button className="btn" onClick={handleOptimize} disabled={optimizing}>
            {optimizing ? 'Rewriting resume…' : 'Generate optimized resume'}
          </button>
        </section>

        {optimized && (
          <section className="panel text-panel optimized-panel">
            <h2>Optimized resume — version {optimized.version}</h2>

            <div className="resume-preview">
              <h3>{optimized.sections.fullName}</h3>
              {optimized.sections.contactInfo && <p className="muted">{optimized.sections.contactInfo}</p>}

              {optimized.sections.summary && (
                <>
                  <h4>Summary</h4>
                  <p>{optimized.sections.summary}</p>
                </>
              )}

              {optimized.sections.skills?.length > 0 && (
                <>
                  <h4>Skills</h4>
                  <p>{optimized.sections.skills.join(' • ')}</p>
                </>
              )}

              {optimized.sections.experience?.length > 0 && (
                <>
                  <h4>Experience</h4>
                  {optimized.sections.experience.map((exp, i) => (
                    <div key={i} className="resume-preview__entry">
                      <strong>
                        {exp.title} — {exp.company} {exp.duration ? `(${exp.duration})` : ''}
                      </strong>
                      <ul>
                        {exp.bullets?.map((b, j) => (
                          <li key={j}>{b}</li>
                        ))}
                      </ul>
                    </div>
                  ))}
                </>
              )}

              {optimized.sections.projects?.length > 0 && (
                <>
                  <h4>Projects</h4>
                  {optimized.sections.projects.map((proj, i) => (
                    <div key={i} className="resume-preview__entry">
                      <strong>{proj.title}</strong>
                      <ul>
                        {proj.bullets?.map((b, j) => (
                          <li key={j}>{b}</li>
                        ))}
                      </ul>
                    </div>
                  ))}
                </>
              )}

              {optimized.sections.education?.length > 0 && (
                <>
                  <h4>Education</h4>
                  <ul>
                    {optimized.sections.education.map((edu, i) => (
                      <li key={i}>{edu}</li>
                    ))}
                  </ul>
                </>
              )}
            </div>

            <div className="export-actions">
              <button className="btn" onClick={() => handleExport('pdf')} disabled={exporting === 'pdf'}>
                {exporting === 'pdf' ? 'Preparing PDF…' : 'Download PDF'}
              </button>
              <button
                className="btn secondary"
                onClick={() => handleExport('docx')}
                disabled={exporting === 'docx'}
              >
                {exporting === 'docx' ? 'Preparing DOCX…' : 'Download DOCX'}
              </button>
            </div>
          </section>
        )}
      </main>

      <style>{`
        .analysis-page {
          max-width: 860px;
          margin: 0 auto;
          padding: 2.5em 2em 5em;
        }
        .back-link {
          display: inline-block;
          font-size: 0.85rem;
          margin-bottom: 1.2em;
          color: var(--ink-soft);
          text-decoration: none;
        }
        .back-link:hover {
          color: var(--stamp);
        }
        .scores-panel {
          display: flex;
          align-items: center;
          gap: 2.5em;
          padding: 1.8em 2em;
          margin-bottom: 1.5em;
        }
        .scores-panel__breakdown {
          display: flex;
          gap: 1.6em;
          flex-wrap: wrap;
        }
        .text-panel {
          padding: 1.6em 1.8em;
          margin-bottom: 1.5em;
        }
        .two-col {
          display: grid;
          grid-template-columns: 1fr 1fr;
          gap: 1.5em;
        }
        .muted {
          color: var(--ink-soft);
          font-size: 0.88rem;
        }
        .tag-list {
          display: flex;
          flex-wrap: wrap;
          gap: 0.5em;
        }
        .tag {
          font-size: 0.82rem;
          padding: 0.3em 0.7em;
          border-radius: 3px;
          font-weight: 600;
        }
        .tag--gap {
          background: var(--gap-bg);
          color: var(--gap);
        }
        .bullet-list, .confirm-list {
          padding-left: 1.2em;
          margin: 0;
        }
        .confirm-list {
          list-style: none;
          padding-left: 0;
          margin: 1em 0 1.4em;
          display: flex;
          flex-direction: column;
          gap: 0.5em;
        }
        .confirm-list label {
          display: flex;
          align-items: center;
          gap: 0.7em;
          font-size: 0.92rem;
          cursor: pointer;
        }
        .resume-preview {
          background: var(--paper);
          border: 1px solid var(--line);
          border-radius: 4px;
          padding: 1.6em 1.8em;
          margin: 1em 0 1.6em;
        }
        .resume-preview h4 {
          font-family: var(--font-sans);
          font-size: 0.78rem;
          text-transform: none;
          color: var(--stamp);
          margin: 1.2em 0 0.4em;
        }
        .resume-preview__entry {
          margin-bottom: 1em;
        }
        .export-actions {
          display: flex;
          gap: 0.8em;
        }
        @media (max-width: 700px) {
          .two-col {
            grid-template-columns: 1fr;
          }
          .scores-panel {
            flex-direction: column;
            align-items: flex-start;
          }
          .export-actions {
            flex-direction: column;
          }
          .export-actions .btn {
            width: 100%;
          }
        }
      `}</style>
    </div>
  )
}
