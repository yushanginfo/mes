[#ftl]
[@b.head/]
[@b.grid items=items var="item"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
    //bar.addItem("导入",action.method('importForm'));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="10%" property="indexno" title="顺序号"/]
    [@b.col width="20%" property="material.code" title="代码"/]
    [@b.col width="35%" property="material.name" title="名称"/]
    [@b.col width="15%" property="material.specification" title="规格"/]
    [@b.col width="15%" property="amount" title="数量"/]
  [/@]
[/@]
[@b.foot/]
