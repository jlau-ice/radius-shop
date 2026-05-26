import request from './request'

export function getDeliveryList() {
  return request.get('/delivery/list')
}

export function saveDelivery(data) {
  return request.post('/delivery', data)
}

export function updateDelivery(id, data) {
  return request.put(`/delivery/${id}`, data)
}

export function deleteDelivery(id) {
  return request.delete(`/delivery/${id}`)
}
