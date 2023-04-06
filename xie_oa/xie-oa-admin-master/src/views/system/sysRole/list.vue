<template>
  <div class="app-container">
    <!--查询表单-->
    <div class="search-div">
    <el-form label-width="70px" size="small">
        <el-row>
        <el-col :span="24">
            <el-form-item label="角色名称">
            <el-input style="width: 100%" v-model="searchObj.roleName" placeholder="角色名称"></el-input>
            </el-form-item>
        </el-col>
        </el-row>
        <el-row style="display:flex">
        <el-button type="primary" icon="el-icon-search" size="mini" :loading="loading" @click="fetchData()">搜索</el-button>
        <el-button type="info" icon="el-icon-refresh" size="mini" @click="resetData">重置</el-button>
        <el-button type="success" icon="el-icon-plus" size="mini" @click="add" :disabled="$hasBP('bnt.sysRole.add') === false">添 加</el-button>
        <el-button type="warning" class="btn-add" size="mini" @click="batchRemove()" >批量删除</el-button>
        </el-row>
    </el-form>
    </div>

    <!-- 表格 -->
    <el-table
    v-loading="listLoading"
    :data="list"
    stripe
    border
    style="width: 100%;margin-top: 10px;"
    @selection-change="handleSelectionChange">

    <el-table-column type="selection"/>

    <el-table-column
        label="序号"
        width="70"
        align="center">
        <template slot-scope="scope">
        {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
    </el-table-column>

    <el-table-column prop="roleName" label="角色名称" />
    <el-table-column prop="roleCode" label="角色编码" />
    <el-table-column prop="createTime" label="创建时间" width="160"/>
    <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
        <el-button type="primary" icon="el-icon-edit" size="mini" @click="edit(scope.row.id)" title="修改"/>
        <el-button type="danger" icon="el-icon-delete" size="mini" @click="removeDataById(scope.row.id)" title="删除"/>
        <el-button type="warning" icon="el-icon-baseball" size="mini" @click="showAssignAuth(scope.row)" title="分配权限"/>
        </template>
    </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
        :current-page="page"
        :total="total"
        :page-size="limit"
        style="padding: 30px 0; text-align: center;"
        layout="total, prev, pager, next, jumper"
        @current-change="fetchData"
    />
    
    <!-- 弹出添加/修改框 -->
    <el-dialog title="添加/修改" :visible.sync="dialogVisible" width="40%" >
      <el-form ref="dataForm" :model="sysRole" label-width="150px" size="small" style="padding-right: 40px;">
        <el-form-item label="角色名称">
          <el-input v-model="sysRole.roleName"/>
        </el-form-item>
        <el-form-item label="角色编码">
          <el-input v-model="sysRole.roleCode"/>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" size="small" icon="el-icon-refresh-right">取 消</el-button>
        <el-button type="primary" icon="el-icon-check" @click="saveOrUpdate()" size="small">确 定</el-button>
      </span>
    </el-dialog>

</div>
</template>

<script>
import api from '@/api/system/sysRole'
import { ConsoleWriter } from 'istanbul-lib-report'
export default {
    data() {
        return {
            list:[],//列表
            total:0,//总数
            page:1,//当前页
            limit:3,//每页记录数
            searchObj:{},//查询对象
            selection:[],//复选框的值
            dialogVisible:false,//默认关闭
            sysRole:{}//对象
        }
    },
    created() {
        this.fetchData()
    },
    methods: {
        //角色添加弹出弹框
        add(){
            this.dialogVisible = true
        },
        //角色修改弹出弹框，查数据
        edit(id){
            //弹出弹框
            this.dialogVisible = true
            //根据id查询数据
            this.findById(id)
        },
        //根据id查询数据,数据回显
        findById(id){
            api.findById(id).then(res=>{
                this.sysRole = res.data
            })
        },
        //添加或修改
        saveOrUpdate(){
            if(!this.sysRole.id){
                this.save()
            }else{
                this.update()
            }
        },
        //添加
        save(){
            api.save(this.sysRole).then(res=>{
                //弹出消息
                this.$message.success(res.message || '添加成功')
                //关闭窗口
                this.dialogVisible = false
                //刷新数据
                this.fetchData()
            })
        },
        //修改
        update(){
            api.update(this.sysRole).then(res=>{
                //弹出消息
                this.$message.success(res.message || '修改成功')
                //关闭窗口
                this.dialogVisible = false
                //刷新数据
                this.fetchData()
            })
        },
        // 跳转到分配菜单的页面
        showAssignAuth(row) {
            this.$router.push('/system/assignAuth?id='+row.id+'&roleName='+row.roleName);
        },
        //分页查询
        fetchData(current = 1){
            this.page = current
            api.getPageList(this.page,this.limit,this.searchObj).then(res=>{
                // console.log(res)
                this.list = res.data.records
                this.total = res.data.total
            })
        },
        //删除数据
        removeDataById(id){
            this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
            }).then(()=>{
                //删除数据
                return api.removeById(id)
            }).then(res=>{
                // 刷新页面
                this.fetchData()
                // 提示信息
                this.$message.success(res.message || '删除成功')
            })
        },
        //得到复选框选择的内容
        handleSelectionChange(selection){
            // console.log(selection)
            this.selection = selection
        },
        //批量删除
        batchRemove(){
            if(this.selection.length === 0){
                this.$message.warning('请选择要删除的记录！')
                return
            }else{
                this.$confirm('此操作将永久删除这些记录, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
                }).then(()=>{
                    //删除数据
                    var idList = []
                    this.selection.forEach(item => {
                        idList.push(item.id)
                    })
                    return api.batchDelete(idList)
                }).then(res=>{
                    // 刷新页面
                    this.fetchData(this.page)
                    // 提示信息
                    this.$message.success(res.message || '删除成功')
                })
            }
            
        }
    },

}
</script>

<style>

</style>