"use strict";
const api_request = require("./request.js");
function login(username, password) {
  return api_request.http.post("/login", { username, password });
}
function getPendingOrders(page = 1) {
  return api_request.http.get("/order/pending", { page, pageSize: 20 });
}
function getDeliveringOrders(page = 1) {
  return api_request.http.get("/order/delivering", { page, pageSize: 20 });
}
function getDoneOrders(page = 1) {
  return api_request.http.get("/order/done", { page, pageSize: 20 });
}
function getOrderDetail(id) {
  return api_request.http.get(`/order/${id}`);
}
function acceptOrder(id) {
  return api_request.http.post(`/order/${id}/accept`);
}
function completeOrder(id, filePath) {
  return api_request.http.upload(`/order/${id}/complete`, filePath);
}
exports.acceptOrder = acceptOrder;
exports.completeOrder = completeOrder;
exports.getDeliveringOrders = getDeliveringOrders;
exports.getDoneOrders = getDoneOrders;
exports.getOrderDetail = getOrderDetail;
exports.getPendingOrders = getPendingOrders;
exports.login = login;
//# sourceMappingURL=../../.sourcemap/mp-weixin/api/index.js.map
