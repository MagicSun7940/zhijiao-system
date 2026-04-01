<template>
  <AppLayout :menu-items="menuItems" />
</template>

<script setup>
import AppLayout from '@/components/AppLayout.vue'

const menuItems = [
  { path: '/student/home',  title: '首页',     icon: 'HomeFilled' },
  { path: '/student/share', title: '教学资源', icon: 'Reading' },
]
</script>
