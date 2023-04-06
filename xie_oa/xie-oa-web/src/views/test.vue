<template>
  <div>
    <div>账号切换</div>
    <button @click="liuxue()" type="default" size="mini">刘雪项目经理</button>
    <button @click="xiejunlong()" type="default" size="mini">谢俊龙总经理</button>
    <button @click="zzjl()" type="default" size="mini">张总经理</button>
    <button @click="admin()" type="default" size="mini">admin</button>
    <div>当前token：{{ token }}</div>
  </div>
</template>

<script>
export default {
  name: "Test",

  data() {
    return {
      token: ''
    };
  },

  created(){
    this.token = window.localStorage.getItem('token')
  },

  methods: {
    admin() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLMwMjAwMDQz0VEqLU4t8kwBikGYeYm5qUAtiSm5mXlKtQDQSdaHQgAAAA.s6LNokpt9a1TnIzwKPzKBPbS-_mJPmSGa2efDmvm0CuHiPvsPuY3UMicP0gXgDN2EX7w2SOjNriJ76Phlwf3Aw'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    liuxue() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLMwMjAxszQx01EqLU4t8kwBiplA2HmJualAPTmZpRWlqUq1AEErn7dEAAAA.sgxcbW8zOvWgsgch4DJY3mxcrW7LHGEtpQqysiAVnZ6TCbddA8sK0C5z_S6LG73hOJeVP10TqECsrS5fQAdD2w'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    zzjl() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNDM3NjMwNLYw11EqLU4t8kxRsjKFMPMSc1OBWqqqsnKUagGHebUNQQAAAA.FcCkD6QfaP8bd_3MYsRD6Tp9wr9JSRFWIUuIdG5O7XxhIeyWfzGLp40_Lesx_kWMMaaskh_ba3NrTlj8-nP6OA'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    xiejunlong() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLMwMjAwMDIx11EqLU4t8kwBikGYeYm5qUAtLzYserK_6-W-mUq1AFhkfoBGAAAA.XxaFPxy1VYCptdlbK6C0tLoumIGn3Z_YbjU2tLsXyZmKNn622YLnxLqt8BNLO6ZoE3GufOAq5UCwyrD77_BETg'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    }
  }
}
</script>
