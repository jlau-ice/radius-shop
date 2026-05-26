import request from './request'

export function getAddressList() {
  return request.get('/address/list')
}

export function saveAddress(data) {
  return request.post('/address', data)
}

export function updateAddress(id, data) {
  return request.put(`/address/${id}`, data)
}

export function deleteAddress(id) {
  return request.delete(`/address/${id}`)
}
