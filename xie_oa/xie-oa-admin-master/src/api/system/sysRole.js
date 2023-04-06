import request from '@/utils/request'

const api = '/admin/system/sysRole'
export default{
    //查询所有角色
    findAll(){
      return request({
        url: `${api}/findAll`,
        method: 'get',
      })
    },
    //分页查询
    getPageList(current,limit,searchObj){
        return request({
            url: `${api}/${current}/${limit}`,
            method: 'get',
            params: searchObj 
          })
    },
    //角色删除
    removeById(id){
        return request({
            url: `${api}/deleteById/${id}`,
            method: 'delete',
          })
    },
    //角色添加
    save(role){
        return request({
            url: `${api}/insert`,
            method: 'post',
            data: role
          })
    },
    //根据id查询数据
    findById(id){
        return request({
            url: `${api}/findById/${id}`,
            method: 'get',
          })
    },
    //角色修改
    update(role){
        return request({
            url: `${api}/update`,
            method: 'put',
            data: role
          })
    },
    //批量删除
    batchDelete(idList){
        return request({
            url: `${api}/batchDelete`,
            method: 'delete',
            data: idList
          })
    },
    //查询角色
    getRoles(adminId) {
        return request({
          url: `${api}/toAssign/${adminId}`,
          method: 'get'
        })
      },
    //更改角色
    assignRoles(assginRoleVo) {
      return request({
          url: `${api}/doAssign`,
          method: 'post',
          data: assginRoleVo
      })
    }
    
}