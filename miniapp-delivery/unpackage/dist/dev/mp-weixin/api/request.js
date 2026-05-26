"use strict";
const common_vendor = require("../common/vendor.js");
const BASE_URL = "http://localhost:8080/api/delivery";
function cleanParams(data) {
  const p = {};
  Object.keys(data).forEach((k) => {
    if (data[k] !== void 0 && data[k] !== null)
      p[k] = data[k];
  });
  return p;
}
function request(url, method = "GET", data = {}) {
  return new Promise((resolve, reject) => {
    common_vendor.index.request({
      url: BASE_URL + url,
      method,
      data: method === "GET" ? cleanParams(data) : data,
      header: {
        "Content-Type": "application/json",
        "token": common_vendor.index.getStorageSync("token") || ""
      },
      success(res) {
        const d = res.data;
        if (d.code === 200)
          resolve(d.data);
        else {
          common_vendor.index.showToast({ title: d.message || "请求失败", icon: "none" });
          if (res.statusCode === 401) {
            common_vendor.index.removeStorageSync("token");
            common_vendor.index.reLaunch({ url: "/pages/login/login" });
          }
          reject(d);
        }
      },
      fail(err) {
        common_vendor.index.showToast({ title: "网络错误", icon: "none" });
        reject(err);
      }
    });
  });
}
const http = {
  get: (url, data) => request(url, "GET", data),
  post: (url, data) => request(url, "POST", data),
  upload: (url, filePath, formData = {}) => {
    return new Promise((resolve, reject) => {
      common_vendor.index.uploadFile({
        url: BASE_URL + url,
        filePath,
        name: "photo",
        formData,
        header: { "token": common_vendor.index.getStorageSync("token") || "" },
        success(res) {
          const d = JSON.parse(res.data);
          if (d.code === 200)
            resolve(d.data);
          else
            reject(d);
        },
        fail: reject
      });
    });
  }
};
exports.http = http;
//# sourceMappingURL=../../.sourcemap/mp-weixin/api/request.js.map
