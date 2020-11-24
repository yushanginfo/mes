[#ftl]
[@b.head/]
[#include "nav.ftl"/]
[@b.toolbar title="评审组信息"]
  bar.addItem("导入","importForm()")
  function importForm(){
    bg.form.submit(document.indexForm,"${b.url('!importForm')}","_blank")
  }
[/@]

<div class="search-container">
    <div class="search-panel">
    [@b.form name="searchForm" action="!search" target="assessGrouplist" title="ui.searchForm" theme="search"]
      [@b.textfields names="assessGroup.code;代码"/]
      [@b.textfields names="assessGroup.name;名称"/]
      <input type="hidden" name="orderBy" value="assessGroup.code desc"/>
    [/@]
    </div>
    <div class="search-list">[@b.div id="assessGrouplist" href="!search?active=1&orderBy=assessGroup.code desc"/]
    </td>
  </tr>
</table>
<br><br>
[@b.form name="indexForm" action="!importForm"/]
[@b.foot/]
