<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    @close="close"
    width="540px"
  >
        <span slot="title" class="drawer-title">
            <b>{{ title }}</b>
        </span>
    <div style="padding: 0 20px">
      <el-form ref="formInline" :rules="rules" :model="formInline" label-width="100px">
        <el-form-item label="用户名" prop="user_name">
          <el-input
            v-model="formInline.user_name"
            placeholder="请输入用户名"
            autocomplete="off"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="roleIds">
          <el-select
            v-model="formInline.roleIds"
            multiple
            placeholder="请选择角色"
            style="width: 100%"
            clearable
          >
            <el-option
              v-for="item in roleList"
              :key="item.id"
              :label="item.role_name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属监区" prop="areaIds">
          <el-select
            v-model="formInline.areaIds"
            multiple
            placeholder="请选择所属监区"
            style="width: 100%"
            clearable
          >
            <el-option
              v-for="item in areaList"
              :key="item.id"
              :label="item.area_name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="姓名" prop="real_name">
          <el-input v-model="formInline.real_name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="formInline.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="帐号状态" prop="isswitch">
          <el-switch v-model="isswitch" active-color="#13ce66"></el-switch>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input type="textarea" :maxlength="200" :rows="5" v-model="formInline.remarks" placeholder="请输入"></el-input>
        </el-form-item>
      </el-form>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取消</el-button>
      <el-button type="primary" :disabled="disabled" :loading="disabled" @click="save('formInline')">
        保存
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
// import { saveUser, selectUserById } from "@/api/systemSet/user";
export default {
  name: "add",
  components: {},
  props: {
    areaList: {
      type: Array,
      default: []
    },
    roleList: {
      type: Array,
      default: []
    }
  },
  data() {
    return {
      loading: false,
      disabled: false,
      title: "",
      dialogFormVisible: false,
      formInline: {
        roleIds: [],
        real_name: "",
        user_name: "",
        areaIds: [],
        phone: "",
        remarks: ""
      },
      isswitch: true,
      rules: {
        roleIds: [{ required: true, message: "请选择", trigger: "blur" }],
        user_name: [{ required: true, message: "请输入", trigger: "blur" }],
        real_name: [{ required: true, message: "请输入", trigger: "blur" }],
        phone: [
          { required: true, message: "请输入", trigger: "blur" },
          {
            validator: function (rule, value, callback) {
              if (/^1[34578]\d{9}$/.test(value) == false) {
                callback(new Error("请输入正确的手机号"));
              } else {
                callback();
              }
            },
            trigger: "blur"
          }
        ],
        areaIds: [{ required: true, message: "请选择", trigger: "blur" }]
      }
    };
  },
  methods: {
    showEdit(row) {
      var that = this;
      if (row.id) {
        // const res = await selectUserById({ id: row.id });
        that.title = "编辑";
        that.formInline = Object.assign({}, row);
      } else {
        that.title = "新增";
        that.formInline = {};
        that.isswitch = true;
      }
      that.dialogFormVisible = true;
      setTimeout(() => {
        that.$refs.formInline.clearValidate();
      }, 300);
    },
    close() {
      this.dialogFormVisible = false;
      setTimeout(() => {
        this.$refs.formInline.clearValidate();
      }, 300);
    },
    save(formName) {
      var that = this;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.disabled = true;
          if (this.isswitch) {
            that.formInline.states = 0;
          } else {
            that.formInline.states = 1;
          }
          this.disabled = true;
          const params = {
            id: that.formInline.id || "",
            roleIds: that.formInline.roleIds || [],
            real_name: that.formInline.real_name || "",
            user_name: that.formInline.user_name || "",
            areaIds: that.formInline.areaIds || [],
            states: that.formInline.states,
            phone: that.formInline.phone,
            remarks: that.formInline.remarks
          };
          saveUser(params)
            .then((res) => {
              this.close();
              this.$emit("fetch-data");
              setTimeout(() => {
                this.disabled = false;
              }, 1000);
            })
            .catch(() => {
              setTimeout(() => {
                this.disabled = false;
              }, 1000);
            });
        } else {
          return false;
        }
      });
    }
  }
};
</script>
