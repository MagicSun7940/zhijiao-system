<template>
  <AppLayout :menu-items="menuItems" />
</template>

<script setup>
import AppLayout from '@/components/AppLayout.vue'

const menuItems = [
  { path: '/admin/dashboard', title: '数据概览', icon: 'DataLine' },
  { path: '/admin/users',     title: '用户管理', icon: 'User' },
  { path: '/admin/posts',     title: '岗位审核', icon: 'Document' },
  { path: '/admin/shares',    title: '分享审核', icon: 'ChatDotRound' },
]
</script>
