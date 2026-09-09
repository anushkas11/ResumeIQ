import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Navbar() {
  const { isAuthenticated, email, logout } = useAuth()
  const navigate = useNavigate()

  return (
    <header className="navbar">
      <Link to="/" className="navbar__brand">
        ResumeIQ
      </Link>

      {isAuthenticated && (
        <nav className="navbar__links">
          <span className="navbar__user">{email}</span>
          <button
            className="btn secondary"
            onClick={() => {
              logout()
              navigate('/login')
            }}
          >
            Log out
          </button>
        </nav>
      )}

      <style>{`
        .navbar {
          display: flex;
          align-items: center;
          justify-content: space-between;
          padding: 1.1em 2em;
          border-bottom: 1px solid var(--line);
          background: var(--panel);
        }
        .navbar__brand {
          font-family: var(--font-serif);
          font-weight: 600;
          font-size: 1.2rem;
          color: var(--ink);
          text-decoration: none;
        }
        .navbar__links {
          display: flex;
          align-items: center;
          gap: 1em;
          min-width: 0;
        }
        .navbar__user {
          font-size: 0.85rem;
          color: var(--ink-soft);
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          max-width: 32vw;
        }
        @media (max-width: 560px) {
          .navbar {
            padding: 0.85em 1em;
          }
          .navbar__links {
            gap: 0.55em;
          }
          .navbar__user {
            max-width: 34vw;
          }
        }
      `}</style>
    </header>
  )
}
