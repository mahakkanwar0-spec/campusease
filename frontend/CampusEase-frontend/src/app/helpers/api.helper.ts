export const API = {
  BASE: 'http://localhost:8080',

  AUTH: {
    REGISTER: '/api/auth/register',
    LOGIN: '/api/auth/login',
    UPDATE: '/api/auth/update',
    DELETE: '/api/auth/delete',
    ALL: '/api/auth/all'
  },

  COMPLAINTS: {
    CREATE: '/api/complaints/create',
    BY_USER: (userId: string) => `/api/complaints/user/${userId}`,
    BY_ID: (id: string) => `/api/complaints/${id}`,
    UPDATE: (id: string) => `/api/complaints/update/${id}`,
    DELETE: (id: string) => `/api/complaints/delete/${id}`,
    ALL: '/api/complaints/all',
    STATUS: (id: string, status: string) => `/api/complaints/${id}/status/${status}`
  }
};