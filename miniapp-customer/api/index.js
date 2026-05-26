import http from './request'

// 用户
export function wxLogin(code, nickname, avatarUrl) {
  return http.post('/user/login', { code, nickname, avatarUrl })
}

export function getUserInfo() {
  return http.get('/user/info')
}

export function logout() {
  return http.post('/user/logout')
}

// 分类
export function getCategories() {
  return http.get('/category')
}

// 商品
export function getProductPage(params) {
  return http.get('/product/page', params)
}

export function getProductDetail(id) {
  return http.get(`/product/${id}`)
}

// 购物车
export function getCartList() {
  return http.get('/cart')
}

export function addToCart(data) {
  return http.post('/cart', data)
}

export function updateCartItem(data) {
  return http.put('/cart', data)
}

export function removeCartItems(ids) {
  return http.delete(`/cart/${ids}`)
}

// 地址
export function getAddressList() {
  return http.get('/address')
}

// 订单
export function submitOrder(data) {
  return http.post('/order/submit', data)
}

export function createPayment(orderId) {
  return http.get(`/order/pay/${orderId}`)
}

export function mockPayOrder(orderId) {
  return http.post(`/order/${orderId}/mock-pay`)
}

export function getOrderPage(params) {
  return http.get('/order/page', params)
}

export function getOrderDetail(id) {
  return http.get(`/order/${id}`)
}

export function cancelOrder(id) {
  return http.put(`/order/${id}/cancel`)
}
