import request from '@/utils/request'

// 申请岗位（FormData，含文件）
export function applyPost(formData) {
  return request.post('/application/apply', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

// 我的申请记录
export function getMyApplications(params) {
  return request.get('/application/my', { params })
}

// 某岗位的申请列表（学校查看）
export function getPostApplications(postId, params) {
  return request.get(`/application/post/${postId}`, { params })
}

// 审核申请
export function reviewApplication(id, status, feedback) {
  return request.put(`/application/review/${id}`, null, {
    params: { status, feedback }
  })
}
