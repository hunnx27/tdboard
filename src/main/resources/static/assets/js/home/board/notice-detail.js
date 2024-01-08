import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'

$(async function(){
    const data = {title:'K-디지털 플랫폼 홈페이지 개설', date: '2023.12.21', content: 'sdfs'}
    var template = document.getElementById("notice-detail-body").innerHTML;
    var result = Mustache.render(template, data);
    document.getElementById("notice-detail-body").innerHTML = result;
    
    
    
    // await useAxios.get('/api/v1/boards/type/notices',
    //     {id: 1}
    //     ,(res)=> {
    //     // console.log('res',res.data)
    // useFilters().YYYYMMDD(data.createdDate)
    //     const tableHeadText = `Total ${res.data.totalElements}건 ${res.data.totalPages} 페이지`
    //     document.getElementById("tableHeadText").innerHTML = tableHeadText

    // },(err)=> {
    //     console.log('err',err)
    // })
})