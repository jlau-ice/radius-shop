import http from './request'

export function login(username, password) {
  return http.post('/login', { username, password })
}

export function getPendingOrders(page = 1) {
  return http.get('/order/pending', { page, pageSize: 20 })
}

export function getDeliveringOrders(page = 1) {
  return http.get('/order/delivering', { page, pageSize: 20 })
}

export function getDoneOrders(page = 1) {
  return http.get('/order/done', { page, pageSize: 20 })
}

export function getOrderDetail(id) {
  return http.get(`/order/${id}`)
}

export function acceptOrder(id) {
  return http.post(`/order/${id}/accept`)
}

export function completeOrder(id, filePath) {
  return http.upload(`/order/${id}/complete`, filePath)
}
