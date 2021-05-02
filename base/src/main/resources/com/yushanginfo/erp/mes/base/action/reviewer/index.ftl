[#ftl]
[@b.head/]
[#include "../assessGroup/nav.ftl"/]
<div class="search-container">
    <div class="search-panel">
    [@b.form name="searchForm" action="!search" target="reviewerlist" title="ui.searchForm" theme="search"]
      [@b.textfields names="reviewer.user.code;工号"/]
      [@b.textfields names="reviewer.user.name;姓名"/]
      <input type="hidden" name="orderBy" value="reviewer.user.code"/>
    [/@]
    </div>
    <div class="search-list">[@b.div id="reviewerlist" href="!search?active=1&orderBy=reviewer.user.code"/]
    </td>
  </tr>
</table>
<br><br>
[@b.form name="indexForm" action="!importForm"/]
[@b.foot/]
