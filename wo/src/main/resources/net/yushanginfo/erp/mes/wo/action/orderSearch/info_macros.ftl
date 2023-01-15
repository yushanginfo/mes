
[#macro info_header title]
<nav class="navbar navbar-expand-lg navbar-light bg-light">
  <a class="navbar-brand" href="#">工单评审</a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>
  <div class="collapse navbar-collapse" id="navbarNavDropdown">
    <ul class="navbar-nav">
      <li class="nav-item [#if !factory??]active[/#if]">
        [@b.a href="!index" class="nav-link"]全部[/@]
      </li>
      [#list factories as f]
      <li class="nav-item [#if factory?? && factory.id=f.id]active[/#if]">
        [@b.a href="!index?factory.id="+f.id class="nav-link"]${f.name}[/@]
      </li>
      [/#list]
    </ul>
  </div>
  <div class="content-header">
    <ol class="breadcrumb  float-sm-right">
      <li class="breadcrumb-item"><i class="fas fa-tachometer-alt" style="margin-top: 6px;"></i>所有工单</li>
      [#if factory??]<li class="breadcrumb-item active">${factory.name}</li>[/#if]
    </ol>
   </div>
</nav>
[/#macro]
