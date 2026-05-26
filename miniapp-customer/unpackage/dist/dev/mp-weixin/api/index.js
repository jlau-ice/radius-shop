"use strict";
const api_request = require("./request.js");
function wxLogin(code, nickname, avatarUrl) {
  return api_request.http.post("/user/login", { code, nickname, avatarUrl });
}
function getUserInfo() {
  return api_request.http.get("/user/info");
}
function logout() {
  return api_request.http.post("/user/logout");
}
function getCategories() {
  return api_request.http.get("/category");
}
function getProductPage(params) {
  return api_request.http.get("/product/page", params);
}
function getProductDetail(id) {
  return api_request.http.get(`/product/${id}`);
}
function getCartList() {
  return api_request.http.get("/cart");
}
function addToCart(data) {
  return api_request.http.post("/cart", data);
}
function updateCartItem(data) {
  return api_request.http.put("/cart", data);
}
function removeCartItems(ids) {
  return api_request.http.delete(`/cart/${ids}`);
}
function getAddressList() {
  return api_request.http.get("/address");
}
function submitOrder(data) {
  return api_request.http.post("/order/submit", data);
}
function createPayment(orderId) {
  return api_request.http.get(`/order/pay/${orderId}`);
}
function mockPayOrder(orderId) {
  return api_request.http.post(`/order/${orderId}/mock-pay`);
}
function getOrderPage(params) {
  return api_request.http.get("/order/page", params);
}
function getOrderDetail(id) {
  return api_request.http.get(`/order/${id}`);
}
function cancelOrder(id) {
  return api_request.http.put(`/order/${id}/cancel`);
}
exports.addToCart = addToCart;
exports.cancelOrder = cancelOrder;
exports.createPayment = createPayment;
exports.getAddressList = getAddressList;
exports.getCartList = getCartList;
exports.getCategories = getCategories;
exports.getOrderDetail = getOrderDetail;
exports.getOrderPage = getOrderPage;
exports.getProductDetail = getProductDetail;
exports.getProductPage = getProductPage;
exports.getUserInfo = getUserInfo;
exports.logout = logout;
exports.mockPayOrder = mockPayOrder;
exports.removeCartItems = removeCartItems;
exports.submitOrder = submitOrder;
exports.updateCartItem = updateCartItem;
exports.wxLogin = wxLogin;
//# sourceMappingURL=../../.sourcemap/mp-weixin/api/index.js.map
