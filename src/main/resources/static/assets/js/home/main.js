import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'
window.onload = function() {
  getNoticeApi(1)
}

async function getNoticeApi(pageNumber){
  await useAxios.get('/api/v1/boards/type/notices',
      {page: pageNumber, size: 5}
      ,(res)=> {
      if(res.data.paging.totalElements > 0){
          let firstPostNumber = res.data.paging.totalElements - (pageNumber - 1) * res.data.paging.pageSize;
          res.data.contents.map((data)=>{
              data.createdDateText = useFilters().YYYYMMDD(data.updatedAt || data.createdAt)
              data.no = firstPostNumber
              firstPostNumber--;
          })
          var template = document.getElementById("notice-table-template").innerHTML;
          var result = Mustache.render(template, res.data);
          document.getElementById("notice-body").innerHTML = result;
      }

  },(err)=> {
      console.log('err',err)
  })

}
