import request from '@/utils/request'

// 用户列表（管理员）
export function getUserList(params) {
  return request.get('/user/list', { params })
}

// 启用/禁用用户（管理员）
export function updateUserStatus(id, status) {
  return request.put(`/user/status/${id}`, null, { params: { status } })
}
