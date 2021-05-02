[#ftl]
[@b.head/]
[#assign materialAssess=workOrder.materialAssess/]
[@b.toolbar title="发起复审"]bar.addBack();[/@]
  [@b.form action="!saveReview" theme="list" name="reviewForm"]
    [@b.field label="工单单号"]${workOrder.orderType.code} ${workOrder.batchNum}[/@]
    [@b.field label="产品图号"]${workOrder.product.specification!}[/@]
    [@b.field label="计划数量"]${workOrder.amount}[/@]
    [@b.field label="材料清单"]
      <div style="margin-left:100px">
      [#list workOrder.product.bom as i]${i.material.name} ${i.material.specification!} [#if i.amount??]${i.amount*workOrder.amount} ${i.material.unit.name}[#else]??[/#if][#if i_has_next]<br>[/#if][/#list]
      </div>
    [/@]
    [#if !materialAssess.ready]
      [#assign dataRange]建议:${b.now?string('yyyy-MM-dd')}~${(workOrder.deadline?string('yyyy-MM-dd'))!"--"}[/#assign]
      [@b.datepicker id="readyOn" name="materialAssess.readyOn" label="到料日期" value=(materialReadyOn)! format="yyyy-MM-dd" comment=dataRange/]
    [#else]
       [@b.field label="到料信息"]有料 <input type="hidden" value="${materialReadyOn?string("yyyy-MM-dd")}" name="materialAssess.readyOn" id="readyOn"/>[/@]
    [/#if]

    [#list workOrder.technics?sort_by("indexno") as wt]
    [#assign technic=wt.technic/]
    [@b.field label=wt.indexno+" "+ wt.technic.name]
        <select name="${wt.id}.factory.id" style="width:100px">
          [#if (wt.factory)??]
            [#assign assessFactory = wt.factory/]
          [#else]
            [#assign assessFactory= workOrder.factory/]
          [/#if]
          [#list factories as f]
            <option value="${f.id}" [#if f.id==assessFactory.id]selected="selected"[/#if]>${f.name}</option>
          [/#list]
        </select>
        <input name="${wt.id}.days" style="width:80px" type="number" value="${wt.days!}" onchange="calcScheduleOn()"/>天
        <label class="comment">
           [#if wt.machine??]${wt.machine.code} ${wt.machine.name}
           [#elseif wt.supplier??] ${wt.supplier.code} ${wt.supplier.name}
           [/#if]
           &nbsp;${wt.description!}
        </label>
    [/@]
    [/#list]
    [@b.field label="计划交期"]${(workOrder.plannedEndOn?string("yyyy-MM-dd"))!"未设置"}[/@]
    [@b.field label="客户交期"]${(workOrder.deadline?string("yyyy-MM-dd"))!"未设置"}[/@]
    [@b.field label="评审交期"]<span id="scheduleOn">${(workOrder.scheduleOn?string("yyyy-MM-dd"))!"--"}[/@]
    [@b.field label="备注"]${(workOrder.remark)?default("无")}[/@]
    [@b.formfoot]
      <input type="hidden" name="workOrder.id" value="${workOrder.id!}"/>
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
  <script>
     var deadline = new Date('${workOrder.deadline?string("yyyy-MM-dd")} 00:00:00')
     function calcScheduleOn(){
       var days=0;
       jQuery("#reviewForm  input[type='number']").each(function (index,elem){
         if(elem.value){
           days += parseInt(elem.value)
         }
       })
       var materialReadyOn=new Date();
       var startOn = jQuery("#readyOn").val();
       if(startOn){
         materialReadyOn = new Date(startOn +" 00:00:00")
       }else{
         alert("请填写到料日期")
       }
       materialReadyOn.setDate(materialReadyOn.getDate() + days);
       if(materialReadyOn > deadline){
         jQuery("#scheduleOn").css("color","red")
       }else{
         jQuery("#scheduleOn").css("color","")
       }
       jQuery("#scheduleOn").text(materialReadyOn.getFullYear()+"-" + (materialReadyOn.getMonth()+1) +"-"+materialReadyOn.getDate()+" "+ days+"天后");
     }
  </script>
[@b.foot/]
