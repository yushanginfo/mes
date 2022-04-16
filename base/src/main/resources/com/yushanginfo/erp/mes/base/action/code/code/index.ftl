[#ftl]
[@b.head/]
[#assign codes={}]
[#assign codes=codes+{'计量单位':'/code/measurement-unit'}]
[#assign codes=codes+{'订单类型':'/code/sales-order-type'}]
[#assign codes=codes+{'工单单别':'/code/work-order-type'}]
[#assign codes=codes+{'品号类型':'/code/material-type'}]

[@b.nav class="nav nav-tabs nav-tabs-compact"  id="code_nav"]
  [#list codes?keys as code]
  [#if code_index<9]
  [#assign link_class]${(code_index==0)?string("nav-link active","nav-link")}[/#assign]
  <li role="presentation" class="nav-item">[@b.a href=codes[code] class=link_class target="codelist"]${code}[/@]</li>
  [/#if]
  [/#list]
  <li class="nav-item dropdown">
          <a href="#" class="nav-link dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">更多.. <span class="caret"></span></a>
          <div class="dropdown-menu">
              [#list codes?keys as code]
              [#if code_index >8]
              [@b.a href=codes[code] class="dropdown-item" target="codelist"]${code}[/@]
              [/#if]
              [/#list]
          </div>
  </li>
[/@]
[@b.div id="codelist" href="/code/measurement-unit"/]
<script>
  jQuery(document).ready(function(){
    jQuery('#code_nav>li>a').bind("click",function(e){
      jQuery("#code_nav>li>a").removeClass("active");
      jQuery(this).addClass("active");
    });
  });
</script>

[@b.foot/]
