import request from '@/utils/request'

// 公开岗位列表（教师/学生浏览）
export function getPostList(params) {
  return request.get('/post/list', { params })
}

// 学校查看自己的岗位（所有状态）
export function getMyPosts(params) {
  return request.get('/post/my', { params })
}

// 管理员查看所有岗位
export function getAdminPostList(params) {
  return request.get('/post/admin/list', { params })
}

// 岗位详情
export function getPostDetail(id) {
  return request.get(`/post/${id}`)
}

// 发布岗位
export function publishPost(data) {
  return request.post('/post/publish', data)
}

// 修改岗位
export function updatePost(id, data) {
  return request.put(`/post/${id}`, data)
}

// 删除岗位
export function deletePost(id) {
  return request.delete(`/post/${id}`)
}

// 管理员审核岗位
export function auditPost(id, status, reason) {
  return request.put(`/post/audit/${id}`, null, { params: { status, reason } })
}
