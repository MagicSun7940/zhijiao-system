<template>
  <AppLayout :menu-items="menuItems" />
</template>

<script setup>
import AppLayout from '@/components/AppLayout.vue'

const menuItems = [
  { path: '/school/posts',       title: '我的岗位', icon: 'Document' },
  { path: '/school/post/create', title: '发布岗位', icon: 'Plus' },
]
</script>
