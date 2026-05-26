import request from './request'

export function getCategoryTree() {
  return request.get('/category/tree')
}

export function getCategoryList() {
  return request.get('/category/list')
}

export function saveCategory(data) {
  return request.post('/category', data)
}

export function updateCategory(id, data) {
  return request.put(`/category/${id}`, data)
}

export function deleteCategory(id) {
  return request.delete(`/category/${id}`)
}

export function uploadCategoryIcon(file) {
  const form = new FormData()
  form.append('file', file)
  return request.post('/category/upload', form, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}
