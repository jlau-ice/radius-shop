const BASE_URL = 'http://localhost:8080/api/delivery'

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
          if (res.statusCode === 401) {
            uni.removeStorageSync('token')
            uni.reLaunch({ url: '/pages/login/login' })
          }
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
  upload: (url, filePath, formData = {}) => {
    return new Promise((resolve, reject) => {
      uni.uploadFile({
        url: BASE_URL + url,
        filePath,
        name: 'photo',
        formData,
        header: { 'token': uni.getStorageSync('token') || '' },
        success(res) {
          const d = JSON.parse(res.data)
          if (d.code === 200) resolve(d.data)
          else reject(d)
        },
        fail: reject
      })
    })
  }
}
