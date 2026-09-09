import { createContext, useContext, useEffect, useMemo, useState } from 'react'
import { authApi } from '../api/resources'
import { API_BASE_URL } from '../api/client'
import client from '../api/client'

const AuthContext = createContext(null)

function decodeJwtEmail(token) {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]))
    return payload.sub || null
  } catch {
    return null
  }
}

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem('resumeiq_token'))
  const [email, setEmail] = useState(() => {
    const existing = localStorage.getItem('resumeiq_token')
    return existing ? decodeJwtEmail(existing) : null
  })

  useEffect(() => {
    if (token) {
      localStorage.setItem('resumeiq_token', token)
      setEmail(decodeJwtEmail(token))
    } else {
      localStorage.removeItem('resumeiq_token')
      setEmail(null)
    }
  }, [token])

  const login = async (credentials) => {
    const response = await authApi.login(credentials)
    setToken(response.data.token)
  }

  const register = async (details) => {
    await authApi.register(details)
    // Registration doesn't log the user in automatically - send them to log in.
  }

  const loginWithToken = (jwt) => {
    setToken(jwt)
  }

  const logout = () => {
    setToken(null)
  }

  const startGoogleLogin = () => {
    window.location.href = `${API_BASE_URL}/oauth2/authorization/google`
  }

  const value = useMemo(
    () => ({
      token,
      email,
      isAuthenticated: Boolean(token),
      login,
      register,
      loginWithToken,
      logout,
      startGoogleLogin
    }),
    [token, email]
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return ctx
}

// Re-exported so components don't need to import the raw client separately
// for one-off calls outside the resources API.
export { client }
