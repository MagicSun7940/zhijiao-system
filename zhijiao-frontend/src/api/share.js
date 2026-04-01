import request from '@/utils/request'

// 管理员查看所有分享（可按状态筛选）
export function getAdminShareList(params) {
  return request.get('/share/admin/list', { params })
}

// 公开分享列表（已发布）
export function getShareList(params) {
  return request.get('/share/list', { params })
}

// 教师查看自己的分享（所有状态）
export function getMyShares(params) {
  return request.get('/share/my', { params })
}

// 分享详情
export function getShareDetail(id) {
  return request.get(`/share/${id}`)
}

// 发布分享
export function publishShare(data) {
  return request.post('/share/publish', data)
}

// 审核分享（管理员）
export function auditShare(id, status) {
  return request.put(`/share/audit/${id}`, null, { params: { status } })
}

// 删除分享
export function deleteShare(id) {
  return request.delete(`/share/${id}`)
}
