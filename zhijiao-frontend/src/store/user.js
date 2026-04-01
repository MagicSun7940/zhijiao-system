import { defineStore } from 'pinia'
import { login as loginApi, logout as logoutApi } from '@/api/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    userId: localStorage.getItem('userId') || null,
    username: localStorage.getItem('username') || '',
    realName: localStorage.getItem('realName') || '',
    role: parseInt(localStorage.getItem('role')) || null,
    avatar: localStorage.getItem('avatar') || '',
    token: localStorage.getItem('token') || ''
  }),

  getters: {
    isAdmin: state => state.role === 1,
    isSchool: state => state.role === 2,
    isTeacher: state => state.role === 3,
    isStudent: state => state.role === 4,
    isLoggedIn: state => !!state.token
  },

  actions: {
    async login(loginData) {
      const res = await loginApi(loginData)
      const { userId, username, realName, role, avatar, token } = res.data
      // 持久化到 localStorage
      this.userId = userId
      this.username = username
      this.realName = realName
      this.role = role
      this.avatar = avatar
      this.token = token
      localStorage.setItem('userId', userId)
      localStorage.setItem('username', username)
      localStorage.setItem('realName', realName)
      localStorage.setItem('role', role)
      localStorage.setItem('avatar', avatar || '')
      localStorage.setItem('token', token)
      return role
    },

    async logout() {
      try {
        await logoutApi()
      } finally {
        this.userId = null
        this.username = ''
        this.realName = ''
        this.role = null
        this.avatar = ''
        this.token = ''
        localStorage.clear()
      }
    }
  }
})
