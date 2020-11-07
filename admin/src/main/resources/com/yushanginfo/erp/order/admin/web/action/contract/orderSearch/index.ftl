[#ftl]
[@b.head/]
[#include "info_macros.ftl"/]
[@info_header title="订单信息"/]
<div class="container">
  <div class="row">
     <div class="col-3">
       <div class="card card-info card-primary card-outline">
         <div class="card-header">
          订单 分类统计
         </div>
         [#assign statusMap={}/]
         [#list statuses as s]
           [#assign statusMap=statusMap+{s?string:s.name}/]
         [/#list]
         <div class="card-body">
             <table class="table table-hover table-sm">
               <thead>
                  <th>状态</th>
                  <th>数量</th>
               </thead>
               <tbody>
               [#list stateStat as stat]
                <tr>
                 <td width="80%">[@b.a href="!search?salesOrder.status="+stat[0] target="order_list"]${statusMap[stat[0]?string]}[/@]</td>
                 <td width="20%">${stat[1]}</td>
                </tr>
                [/#list]
               </tbody>
             </table>
         </div>

         <div class="card-body">
             <table class="table table-hover table-sm">
               <thead>
                  <th>品号属性</th>
                  <th>数量</th>
               </thead>
               <tbody>
               [#list materialTypeStat as stat]
                <tr>
                 <td width="80%">[@b.a href="!search?salesOrder.product.materialType.id="+stat[0] target="order_list"]${stat[1]}[/@]</td>
                 <td width="20%">${stat[2]}</td>
                </tr>
                [/#list]
               </tbody>
             </table>
         </div>
       </div>
     </div><!--end col-3-->
     [@b.div class="col-9" id="order_list" href="!search"]
     [/@]
  </div>
</div>
[@b.foot/]
