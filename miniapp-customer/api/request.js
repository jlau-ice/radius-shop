const BASE_URL = 'http://localhost:8080/api/app'

function cleanParams(data) {
  const p = {}
  Object.keys(data).forEach(k => { if (data[k] !== undefined && data[k] !== null) p[k] = data[k] })
  return p
}

function request(url, method = 'GET', data = {}) {
  return new Promise((resolve, reject) => {
    uni.request({
      url: BASE_URL + url,
      method,
      data: method === 'GET' ? cleanParams(data) : data,
      header: {
        'Content-Type': 'application/json',
        'token': uni.getStorageSync('token') || ''
      },
      success(res) {
        const d = res.data
        if (d.code === 200) resolve(d.data)
        else {
          uni.showToast({ title: d.message || '请求失败', icon: 'none' })
          reject(d)
        }
      },
      fail(err) {
        uni.showToast({ title: '网络错误', icon: 'none' })
        reject(err)
      }
    })
  })
}

export default {
  get: (url, data) => request(url, 'GET', data),
  post: (url, data) => request(url, 'POST', data),
  put: (url, data) => request(url, 'PUT', data),
  delete: (url, data) => request(url, 'DELETE', data)
}
