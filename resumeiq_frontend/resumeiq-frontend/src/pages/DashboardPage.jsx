import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { resumeApi, jobDescriptionApi, analysisApi } from '../api/resources'
import Navbar from '../components/Navbar'

export default function DashboardPage() {
  const navigate = useNavigate()

  const [resumes, setResumes] = useState([])
  const [jobDescriptions, setJobDescriptions] = useState([])

  const [selectedResumeId, setSelectedResumeId] = useState(null)
  const [selectedJdId, setSelectedJdId] = useState(null)

  const [uploading, setUploading] = useState(false)
  const [uploadError, setUploadError] = useState('')

  const [jdCompany, setJdCompany] = useState('')
  const [jdRole, setJdRole] = useState('')
  const [jdText, setJdText] = useState('')
  const [savingJd, setSavingJd] = useState(false)
  const [jdError, setJdError] = useState('')

  const [analyzing, setAnalyzing] = useState(false)
  const [analyzeError, setAnalyzeError] = useState('')

  const loadResumes = async () => {
    const res = await resumeApi.list()
    setResumes(res.data)
  }

  const loadJobDescriptions = async () => {
    const res = await jobDescriptionApi.list()
    setJobDescriptions(res.data)
  }

  useEffect(() => {
    loadResumes()
    loadJobDescriptions()
  }, [])

  const handleUpload = async (e) => {
    const file = e.target.files[0]
    if (!file) return
    setUploading(true)
    setUploadError('')
    try {
      const res = await resumeApi.upload(file)
      setResumes((prev) => [res.data, ...prev])
      setSelectedResumeId(res.data.id)
    } catch (err) {
      setUploadError(err.response?.data?.message || 'Could not read that file. Try a PDF or DOCX resume.')
    } finally {
      setUploading(false)
      e.target.value = ''
    }
  }

  const handleAddJd = async (e) => {
    e.preventDefault()
    setSavingJd(true)
    setJdError('')
    try {
      const res = await jobDescriptionApi.create({
        companyName: jdCompany,
        roleTitle: jdRole,
        rawText: jdText
      })
      setJobDescriptions((prev) => [res.data, ...prev])
      setSelectedJdId(res.data.id)
      setJdCompany('')
      setJdRole('')
      setJdText('')
    } catch (err) {
      setJdError(err.response?.data?.message || 'Could not save that job description.')
    } finally {
      setSavingJd(false)
    }
  }

  const handleAnalyze = async () => {
    if (!selectedResumeId || !selectedJdId) return
    setAnalyzing(true)
    setAnalyzeError('')
    try {
      const res = await analysisApi.create({
        resumeId: selectedResumeId,
        jobDescriptionId: selectedJdId
      })
      navigate(`/analysis/${res.data.id}`)
    } catch (err) {
      setAnalyzeError(err.response?.data?.message || 'Analysis failed. Please try again.')
    } finally {
      setAnalyzing(false)
    }
  }

  return (
    <div>
      <Navbar />

      <main className="dashboard">
        <h1>Match a resume to a job</h1>
        <p className="dashboard__intro">
          Pick a resume and a job description below, then run the gap analysis.
        </p>

        <div className="dashboard__grid">
          <section className="panel dashboard__section">
            <h2>1. Resume</h2>
            <p className="dashboard__hint">Upload a PDF or DOCX. Text is extracted automatically.</p>

            <label className="upload-drop">
              <input type="file" accept=".pdf,.doc,.docx" onChange={handleUpload} hidden />
              {uploading ? 'Reading file…' : 'Choose a file to upload'}
            </label>

            {uploadError && <div className="error-banner">{uploadError}</div>}

            <ul className="pick-list">
              {resumes.length === 0 && <li className="pick-list__empty">No resumes uploaded yet.</li>}
              {resumes.map((r) => (
                <li key={r.id}>
                  <label className="pick-list__item">
                    <input
                      type="radio"
                      name="resume"
                      checked={selectedResumeId === r.id}
                      onChange={() => setSelectedResumeId(r.id)}
                    />
                    <span>{r.fileName}</span>
                  </label>
                </li>
              ))}
            </ul>
          </section>

          <section className="panel dashboard__section">
            <h2>2. Job description</h2>
            <p className="dashboard__hint">Paste the role you're targeting.</p>

            <form onSubmit={handleAddJd} className="jd-form">
              <div className="field">
                <label htmlFor="company">Company (optional)</label>
                <input id="company" value={jdCompany} onChange={(e) => setJdCompany(e.target.value)} />
              </div>
              <div className="field">
                <label htmlFor="role">Role title (optional)</label>
                <input id="role" value={jdRole} onChange={(e) => setJdRole(e.target.value)} />
              </div>
              <div className="field">
                <label htmlFor="jdText">Job description text</label>
                <textarea
                  id="jdText"
                  rows={5}
                  required
                  value={jdText}
                  onChange={(e) => setJdText(e.target.value)}
                />
              </div>
              {jdError && <div className="error-banner">{jdError}</div>}
              <button className="btn secondary" type="submit" disabled={savingJd}>
                {savingJd ? 'Saving…' : 'Save job description'}
              </button>
            </form>

            <ul className="pick-list">
              {jobDescriptions.length === 0 && (
                <li className="pick-list__empty">No job descriptions saved yet.</li>
              )}
              {jobDescriptions.map((jd) => (
                <li key={jd.id}>
                  <label className="pick-list__item">
                    <input
                      type="radio"
                      name="jd"
                      checked={selectedJdId === jd.id}
                      onChange={() => setSelectedJdId(jd.id)}
                    />
                    <span>
                      {jd.roleTitle || 'Untitled role'}
                      {jd.companyName ? ` · ${jd.companyName}` : ''}
                    </span>
                  </label>
                </li>
              ))}
            </ul>
          </section>
        </div>

        <div className="dashboard__action">
          {analyzeError && <div className="error-banner">{analyzeError}</div>}
          <button
            className="btn"
            disabled={!selectedResumeId || !selectedJdId || analyzing}
            onClick={handleAnalyze}
          >
            {analyzing ? 'Analyzing…' : 'Run gap analysis'}
          </button>
        </div>
      </main>

      <style>{`
        .dashboard {
          max-width: 960px;
          margin: 0 auto;
          padding: 3em 2em 5em;
        }
        .dashboard__intro {
          color: var(--ink-soft);
        }
        .dashboard__grid {
          display: grid;
          grid-template-columns: 1fr 1fr;
          gap: 1.5em;
          margin-top: 2em;
        }
        .dashboard__section {
          padding: 1.6em;
        }
        .dashboard__hint {
          font-size: 0.85rem;
          color: var(--ink-soft);
          margin-bottom: 1em;
        }
        .upload-drop {
          display: flex;
          align-items: center;
          justify-content: center;
          border: 1.5px dashed var(--line-strong);
          border-radius: 4px;
          padding: 1.4em;
          font-size: 0.9rem;
          color: var(--ink-soft);
          cursor: pointer;
          text-align: center;
          margin-bottom: 1em;
        }
        .upload-drop:hover {
          border-color: var(--stamp);
          color: var(--stamp);
        }
        .pick-list {
          list-style: none;
          margin: 1em 0 0;
          padding: 0;
          display: flex;
          flex-direction: column;
          gap: 0.4em;
          max-height: 220px;
          overflow-y: auto;
        }
        .pick-list__empty {
          font-size: 0.85rem;
          color: var(--ink-soft);
          padding: 0.4em 0;
        }
        .pick-list__item {
          display: flex;
          align-items: center;
          gap: 0.6em;
          padding: 0.5em 0.6em;
          border-radius: 3px;
          font-size: 0.9rem;
          cursor: pointer;
        }
        .pick-list__item:hover {
          background: var(--paper);
        }
        .jd-form textarea {
          resize: vertical;
        }
        .dashboard__action {
          margin-top: 2em;
          display: flex;
          flex-direction: column;
          align-items: flex-start;
          gap: 0.8em;
        }
        @media (max-width: 760px) {
          .dashboard__grid {
            grid-template-columns: 1fr;
          }
        }
      `}</style>
    </div>
  )
}
