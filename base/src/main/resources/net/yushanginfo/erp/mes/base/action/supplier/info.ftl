[#ftl]
[@b.head/]
[@b.toolbar title="供应商"]
  bar.addBack("${b.text("action.back")}");
[/@]

[@b.card class="card-info card-outline"]
  [@b.card_header]
         <i class="fas fa-school"></i>&nbsp;${supplier.name}<span style="font-size:0.8em">(${supplier.code})</span>
   [/@]
    [@b.grid items=technics sortable="false" var="technic" ]
        [@b.row]
            [@b.col title="序号" width="5%"]${technic_index+1}[/@]
            [@b.col property="code" title="编码" width="15%"/]
            [@b.col property="name"  title="名称" width="25%"/]
            [@b.col property="description" title="说明" width="55%"/]
        [/@]
    [/@]
[/@]

[@b.foot/]
