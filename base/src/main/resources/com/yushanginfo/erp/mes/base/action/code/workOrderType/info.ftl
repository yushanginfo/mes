[#ftl]
[@b.head/]
[@b.toolbar title="工单类型"]
  bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
  <tr>
    <td class="title" width="20%">代码</td>
    <td class="content">${orderType.code}</td>
  </tr>
  <tr>
    <td class="title" width="20%">名称</td>
    <td class="content">${orderType.name}</td>
  </tr>
</table>

[@b.foot/]
