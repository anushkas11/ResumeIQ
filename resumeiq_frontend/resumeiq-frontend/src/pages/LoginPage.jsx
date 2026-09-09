import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import AuthLayout from '../components/AuthLayout'

export default function LoginPage() {
  const { login, startGoogleLogin } = useAuth()
  const navigate = useNavigate()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      await login({ email, password })
      navigate('/dashboard')
    } catch (err) {
      setError(err.response?.data?.message || 'Could not log in. Check your email and password.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <AuthLayout title="Log in">
      {error && <div className="error-banner">{error}</div>}

      <form onSubmit={handleSubmit}>
        <div className="field">
          <label htmlFor="email">Email</label>
          <input
            id="email"
            type="email"
            required
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
        </div>

        <div className="field">
          <label htmlFor="password">Password</label>
          <input
            id="password"
            type="password"
            required
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>

        <button className="btn auth-action" type="submit" disabled={loading}>
          {loading ? 'Logging in…' : 'Log in'}
        </button>
      </form>

      <div className="divider">
        <span>or</span>
      </div>

      <button className="btn secondary auth-action" onClick={startGoogleLogin}>
        Continue with Google
      </button>

      <p className="switch-link">
        New here? <Link to="/register">Create an account</Link>
      </p>

      <style>{authStyles}</style>
    </AuthLayout>
  )
}

const authStyles = `
  .auth-action { width: 100%; }
  .divider {
    display: flex;
    align-items: center;
    gap: 0.8em;
    margin: 1.3em 0;
    color: var(--ink-soft);
    font-size: 0.8rem;
  }
  .divider::before,
  .divider::after {
    content: '';
    flex: 1;
    height: 1px;
    background: var(--line);
  }
  .switch-link {
    margin-top: 1.4em;
    font-size: 0.88rem;
    color: var(--ink-soft);
  }
`
