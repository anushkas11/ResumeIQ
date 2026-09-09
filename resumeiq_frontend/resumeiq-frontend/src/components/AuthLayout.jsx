export default function AuthLayout({ eyebrow, title, children }) {
  return (
    <div className="auth-layout">
      <div className="auth-layout__side">
        <div className="auth-layout__topline">
          <div className="auth-layout__mark">ResumeIQ</div>
          <span className="auth-layout__badge">Career clarity, focused</span>
        </div>
        <div className="auth-layout__copy">
          <p className="auth-layout__kicker">Your next opportunity starts here</p>
          <h1>Tailor every resume to the job in front of you.</h1>
        </div>
        <p>
          Upload a resume and a job description. Get a real ATS gap analysis,
          then a rewritten, keyword-matched resume you can download — built
          only from what's actually true about you.
        </p>
        <div className="auth-layout__stats" aria-label="ResumeIQ features">
          <span><strong>01</strong> Find the gaps</span>
          <span><strong>02</strong> Sharpen the story</span>
        </div>
      </div>

      <div className="auth-layout__form">
        <div className="auth-layout__card panel">
          {eyebrow && <div className="auth-layout__eyebrow">{eyebrow}</div>}
          <h2>{title}</h2>
          {children}
        </div>
      </div>

      <style>{`
        .auth-layout {
          min-height: 100vh;
          display: grid;
          grid-template-columns: minmax(0, 1.1fr) minmax(360px, 0.9fr);
          background: var(--paper);
        }
        .auth-layout__side {
          position: relative;
          overflow: hidden;
          background: var(--ink);
          color: var(--paper);
          padding: clamp(2.5em, 7vw, 6em) clamp(2em, 7vw, 7em);
          display: flex;
          flex-direction: column;
          justify-content: center;
        }
        .auth-layout__side::after {
          content: '';
          position: absolute;
          width: 22em;
          height: 22em;
          right: -8em;
          bottom: -9em;
          border: 1px solid rgba(242, 243, 239, 0.2);
          border-radius: 50%;
          box-shadow: 0 0 0 2.5em rgba(242, 243, 239, 0.04), 0 0 0 5.5em rgba(242, 243, 239, 0.03);
        }
        .auth-layout__topline,
        .auth-layout__copy,
        .auth-layout__side > p,
        .auth-layout__stats {
          position: relative;
          z-index: 1;
        }
        .auth-layout__topline {
          display: flex;
          align-items: center;
          justify-content: space-between;
          gap: 1em;
          margin-bottom: clamp(3em, 10vh, 7em);
        }
        .auth-layout__mark {
          font-family: var(--font-serif);
          font-size: 1.1rem;
          opacity: 0.85;
        }
        .auth-layout__badge {
          color: #e8b978;
          font-size: 0.7rem;
          letter-spacing: 0.08em;
          text-transform: uppercase;
        }
        .auth-layout__kicker {
          color: #e8b978;
          font-size: 0.75rem;
          font-weight: 600;
          letter-spacing: 0.12em;
          text-transform: uppercase;
          margin-bottom: 1.1em;
        }
        .auth-layout__side h1 {
          color: var(--paper);
          font-size: clamp(2.2rem, 4.5vw, 4.3rem);
          line-height: 1.04;
          max-width: 14ch;
        }
        .auth-layout__side p {
          color: #c7cdd9;
          max-width: 42ch;
          margin-top: 1.8em;
        }
        .auth-layout__stats {
          display: flex;
          flex-wrap: wrap;
          gap: 1.5em;
          margin-top: 2.5em;
          color: #c7cdd9;
          font-size: 0.8rem;
        }
        .auth-layout__stats span {
          display: flex;
          align-items: center;
          gap: 0.55em;
        }
        .auth-layout__stats strong {
          color: #e8b978;
          font-size: 0.7rem;
          letter-spacing: 0.08em;
        }
        .auth-layout__form {
          display: flex;
          align-items: center;
          justify-content: center;
          padding: clamp(1.25em, 5vw, 4em);
        }
        .auth-layout__card {
          width: 100%;
          max-width: 430px;
          padding: clamp(1.5em, 4vw, 2.8em);
          box-shadow: 0 18px 50px rgba(22, 33, 58, 0.08);
        }
        .auth-layout__eyebrow {
          font-size: 0.8rem;
          color: var(--ink-soft);
          margin-bottom: 0.5em;
        }
        @media (max-width: 860px) {
          .auth-layout {
            grid-template-columns: 1fr;
          }
          .auth-layout__side {
            min-height: auto;
            padding: 2.5em 1.5em 2.25em;
          }
          .auth-layout__topline {
            margin-bottom: 3em;
          }
          .auth-layout__side h1 {
            max-width: 18ch;
          }
          .auth-layout__side > p {
            max-width: 58ch;
            margin-top: 1.25em;
          }
          .auth-layout__stats {
            margin-top: 1.5em;
          }
          .auth-layout__form {
            align-items: flex-start;
            padding: 2em 1.5em 3em;
          }
        }
        @media (max-width: 480px) {
          .auth-layout__badge {
            display: none;
          }
          .auth-layout__topline {
            margin-bottom: 2.5em;
          }
          .auth-layout__side h1 {
            font-size: 2.35rem;
          }
          .auth-layout__side > p {
            font-size: 0.92rem;
          }
        }
      `}</style>
    </div>
  )
}
