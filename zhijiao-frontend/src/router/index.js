import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/common/LoginView.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/common/RegisterView.vue'),
    meta: { title: '注册' }
  },
  // ===== 管理员 =====
  {
    path: '/admin',
    component: () => import('@/views/admin/AdminLayout.vue'),
    meta: { requireAuth: true, role: 1 },
    children: [
      { path: '', redirect: '/admin/dashboard' },
      { path: 'dashboard', component: () => import('@/views/admin/DashboardView.vue'), meta: { title: '数据概览' } },
      { path: 'users', component: () => import('@/views/admin/UserManageView.vue'), meta: { title: '用户管理' } },
      { path: 'posts', component: () => import('@/views/admin/PostAuditView.vue'), meta: { title: '岗位审核' } },
      { path: 'shares', component: () => import('@/views/admin/ShareAuditView.vue'), meta: { title: '分享审核' } },
    ]
  },
  // ===== 学校用户 =====
  {
    path: '/school',
    component: () => import('@/views/school/SchoolLayout.vue'),
    meta: { requireAuth: true, role: 2 },
    children: [
      { path: '', redirect: '/school/posts' },
      { path: 'posts', component: () => import('@/views/school/PostListView.vue'), meta: { title: '我的岗位' } },
      { path: 'post/create', component: () => import('@/views/school/PostFormView.vue'), meta: { title: '发布岗位' } },
      { path: 'post/edit/:id', component: () => import('@/views/school/PostFormView.vue'), meta: { title: '编辑岗位' } },
      { path: 'applications/:postId', component: () => import('@/views/school/ApplicationListView.vue'), meta: { title: '申请管理' } },
    ]
  },
  // ===== 志愿教师 =====
  {
    path: '/teacher',
    component: () => import('@/views/teacher/TeacherLayout.vue'),
    meta: { requireAuth: true, role: 3 },
    children: [
      { path: '', redirect: '/teacher/posts' },
      { path: 'posts', component: () => import('@/views/teacher/PostBrowseView.vue'), meta: { title: '浏览岗位' } },
      { path: 'my-applications', component: () => import('@/views/teacher/MyApplicationView.vue'), meta: { title: '我的申请' } },
      { path: 'share', component: () => import('@/views/teacher/ShareListView.vue'), meta: { title: '教学分享' } },
      { path: 'share/create', component: () => import('@/views/teacher/ShareFormView.vue'), meta: { title: '发布分享' } },
    ]
  },
  // ===== 学生 =====
  {
    path: '/student',
    component: () => import('@/views/student/StudentLayout.vue'),
    meta: { requireAuth: true, role: 4 },
    children: [
      { path: '', redirect: '/student/home' },
      { path: 'home', component: () => import('@/views/student/StudentHomeView.vue'), meta: { title: '首页' } },
      { path: 'share', component: () => import('@/views/student/ShareBrowseView.vue'), meta: { title: '教学资源' } },
    ]
  },
  { path: '/', redirect: '/login' },
  { path: '/:pathMatch(.*)*', redirect: '/login' }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 导航守卫
router.beforeEach((to, from, next) => {
  document.title = (to.meta.title ? `${to.meta.title} - ` : '') + '支教管理系统'

  const token = localStorage.getItem('token')
  const role = parseInt(localStorage.getItem('role'))

  if (to.meta.requireAuth) {
    if (!token) {
      next('/login')
      return
    }
    // 角色权限校验
    if (to.meta.role && to.meta.role !== role) {
      const roleMap = { 1: '/admin', 2: '/school', 3: '/teacher', 4: '/student' }
      next(roleMap[role] || '/login')
      return
    }
  }
  next()
})

export default router
