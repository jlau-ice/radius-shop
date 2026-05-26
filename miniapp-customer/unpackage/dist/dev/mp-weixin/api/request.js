"use strict";
const common_vendor = require("../common/vendor.js");
const BASE_URL = "http://localhost:8080/api/app";
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
  put: (url, data) => request(url, "PUT", data),
  delete: (url, data) => request(url, "DELETE", data)
};
exports.http = http;
//# sourceMappingURL=../../.sourcemap/mp-weixin/api/request.js.map
