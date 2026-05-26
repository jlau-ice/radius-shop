import request from './request'

export function getOrderPage(params) {
  return request.get('/order/page', { params })
}

export function getOrderDetail(id) {
  return request.get(`/order/${id}`)
}

export function updateOrderStatus(id, status) {
  return request.put(`/order/${id}/status`, null, { params: { status } })
}
