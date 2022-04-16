[#ftl]
[@b.head/]
[@b.toolbar title="产品材料清单基本信息"]
  bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
  <tr>
    <td class="title" width="20%">产品代码</td>
    <td class="content">${item.product.code}</td>
  </tr>
  <tr>
    <td class="title" width="20%">名称</td>
    <td class="content">${item.product.name}</td>
  </tr>
  <tr>
    <td class="title" width="20%">材料代码</td>
    <td class="content">${item.material.code}</td>
  </tr>
  <tr>
    <td class="title" width="20%">材料名称</td>
    <td class="content">${item.material.name}</td>
  </tr>
</table>

[@b.foot/]
