import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import AuthLayout from '../components/AuthLayout'

export default function RegisterPage() {
  const { register, startGoogleLogin } = useAuth()
  const navigate = useNavigate()
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      await register({ name, email, password })
      navigate('/login', { state: { justRegistered: true } })
    } catch (err) {
      setError(err.response?.data?.message || 'Could not create your account. Try a different email.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <AuthLayout title="Create your account">
      {error && <div className="error-banner">{error}</div>}

      <form onSubmit={handleSubmit}>
        <div className="field">
          <label htmlFor="name">Name</label>
          <input id="name" type="text" required value={name} onChange={(e) => setName(e.target.value)} />
        </div>

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
            minLength={8}
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>

        <button className="btn auth-action" type="submit" disabled={loading}>
          {loading ? 'Creating account…' : 'Create account'}
        </button>
      </form>

      <div className="divider">
        <span>or</span>
      </div>

      <button className="btn secondary auth-action" onClick={startGoogleLogin}>
        Continue with Google
      </button>

      <p className="switch-link">
        Already have an account? <Link to="/login">Log in</Link>
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
