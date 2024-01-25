import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'



window.onload = function () {
    const facilitiesId = document.getElementById('facilitiesId').value

    getFacilitiesApi(facilitiesId)

    //initElementSetting()

    debugger;
    $(".datepicker").datepicker({
        dateFormat: 'yy-mm-dd',
        onSelect: function(dateText, inst) {

        }
    });
};

async function getFacilitiesApi(id){
    await useAxios.get(`/api/v1/facilities/${id}`,
        {page: 1}
        ,(res)=> {
        console.log('res',res.data)
        if(res.data){
            const facilitiesImg = document.getElementById('facilitiesImg')
            facilitiesImg.setAttribute('src',res.data.imageUrl)
            const facilitiesTitle = document.getElementById('facilitiesTitle')
            facilitiesTitle.innerText = res.data.name
            
            const applicationBtn = document.getElementById("applicationBtn")
            applicationBtn.addEventListener('click', () =>{
                //api
                alert('신청 완료')
            })
        }
    },(err)=> {
        console.log('err',err)
    })

}

function initElementSetting(){
    // 현재 년도 가져오기
    const currentYear = new Date().getFullYear();

    // 옵션 추가: 현재 년도부터 현재 년도 + 2년까지
    const yearOptionData = []
    for (let year = currentYear; year <= currentYear + 2; year++) {
        yearOptionData.push({
            'value' : year,
            'text' : year
        });
    }

    var template = document.getElementById("select-option-template").innerHTML;
    var result = Mustache.render(template, {options: yearOptionData});
    document.getElementById("yearSelectOption").innerHTML = result;


    // 옵션 추가: 1월부터 12월까지
    const monthOptionData = []
    for (let month = 1; month <= 12; month++) {
        monthOptionData.push({
            'value' : month,
            'text' : month
        });
    }

    var template = document.getElementById("select-option-template").innerHTML;
    var result = Mustache.render(template, {options: monthOptionData});
    document.getElementById("monthSelectOption").innerHTML = result;


    // 옵션 추가: 1일부터 31일까지
    const dayOptionData = []
    for (let day = 1; day <= 31; day++) {
        dayOptionData.push({
            'value' : day,
            'text' : day
        });
    }
    var template = document.getElementById("select-option-template").innerHTML;
    var result = Mustache.render(template, {options: dayOptionData});
    document.getElementById("daySelectOption").innerHTML = result;

    // 옵션 추가: 00부터 23까지
    const hourOptionData = []
    for (let hour = 0; hour <= 23; hour++) {
        hourOptionData.push({
            'value' : hour,
            'text' : hour
        });
    }
    var template = document.getElementById("select-option-template").innerHTML;
    var result = Mustache.render(template, {options: hourOptionData});
    document.getElementById("hourSelectOption").innerHTML = result;

    // 옵션 추가: 0분, 30분
    const minuteOptionData = []
    for (let minute = 0; minute <= 30; minute += 30) {
        minuteOptionData.push({
            'value' : minute,
            'text' : minute
        });
    }
    var template = document.getElementById("select-option-template").innerHTML;
    var result = Mustache.render(template, {options: minuteOptionData});
    document.getElementById("minuteSelectOption").innerHTML = result;
}