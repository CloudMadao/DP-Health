import request from '@/utils/request'

//按县区获取分组信息
export function groupCountry(query) {
  return request({
    url: '/dp/countrycount',
    method: 'get',
    params: query
  })
}

export function groupCountryPost(query) {
  return request({
    url: '/dp/countrycount',
    method: 'post',
    params: query
  })
}


export function groupihitPost(query) {
  return request({
    url: '/ttjm/dpcount/ihitcount',
    method: 'post',
    params: query
  })
}

