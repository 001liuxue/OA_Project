import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/admin/system/index/table/list',
    method: 'get',
    params
  })
}
