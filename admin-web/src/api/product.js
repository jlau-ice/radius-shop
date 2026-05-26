import request from './request'

export function getProductPage(params) {
  return request.get('/product/page', { params })
}

export function getProductDetail(id) {
  return request.get(`/product/${id}`)
}

export function saveProduct(data) {
  return request.post('/product', data)
}

export function updateProduct(id, data) {
  return request.put(`/product/${id}`, data)
}

export function deleteProduct(id) {
  return request.delete(`/product/${id}`)
}

export function toggleProductStatus(id, status) {
  return request.put(`/product/${id}/status`, null, { params: { status } })
}

export function uploadImage(file) {
  const form = new FormData()
  form.append('file', file)
  return request.post('/product/upload', form, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}
