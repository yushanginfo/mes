[#ftl]
[@b.head/]
[#include "../assessGroup/nav.ftl"/]
[@b.toolbar title="评审组成员信息"]
  bar.addItem("导入","importForm()")
  function importForm(){
    bg.form.submit(document.indexForm,"${b.url('!importForm')}","_blank")
  }
[/@]

<div class="search-container">
    <div class="search-panel">
    [@b.form name="searchForm" action="!search" target="assessMemberlist" title="ui.searchForm" theme="search"]
      [@b.textfields names="assessMember.user.code;工号"/]
      [@b.textfields names="assessMember.user.name;姓名"/]
      [@b.textfields names="assessMember.name;评审组"/]
      <input type="hidden" name="orderBy" value="assessMember.group.code desc"/>
    [/@]
    </div>
    <div class="search-list">[@b.div id="assessMemberlist" href="!search?active=1&orderBy=assessMember.group.code desc"/]
    </td>
  </tr>
</table>
<br><br>
[@b.form name="indexForm" action="!importForm"/]
[@b.foot/]
