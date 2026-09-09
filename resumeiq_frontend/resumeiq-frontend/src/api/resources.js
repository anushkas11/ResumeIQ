import client from './client'

export const authApi = {
  register: (data) => client.post('/api/users', data),
  login: (data) => client.post('/api/auth/login', data)
}

export const resumeApi = {
  upload: (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return client.post('/api/resumes/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },
  list: () => client.get('/api/resumes'),
  getById: (id) => client.get(`/api/resumes/${id}`)
}

export const jobDescriptionApi = {
  create: (data) => client.post('/api/job-descriptions', data),
  list: () => client.get('/api/job-descriptions'),
  getById: (id) => client.get(`/api/job-descriptions/${id}`)
}

export const analysisApi = {
  create: (data) => client.post('/api/analyses', data),
  getById: (id) => client.get(`/api/analyses/${id}`),
  listByResume: (resumeId) => client.get(`/api/analyses/resume/${resumeId}`)
}

export const optimizeApi = {
  optimize: (data) => client.post('/api/optimize', data),
  getById: (id) => client.get(`/api/optimize/${id}`),
  // Export requires the auth header, so we fetch as a blob rather than
  // linking directly to the URL (a plain <a href> wouldn't carry the JWT).
  exportFile: (id, format) =>
    client.get(`/api/optimize/${id}/export`, {
      params: { format },
      responseType: 'blob'
    })
}

export function downloadBlob(blob, filename) {
  const url = window.URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = filename
  document.body.appendChild(link)
  link.click()
  link.remove()
  window.URL.revokeObjectURL(url)
}
