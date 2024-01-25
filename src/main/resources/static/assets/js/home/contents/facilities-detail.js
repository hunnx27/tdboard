import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'

window.onload = function () {

    // init
    const facilitiesId = document.getElementById('facilitiesId').value
    getFacilitiesApi(facilitiesId)
    const today = new Date();
    const todayText = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`;
    getAvailiableTime(facilitiesId, todayText); // 학습가능 시간 조회

    // event
    $(".datepicker").datepicker({
        dateFormat: 'yy-mm-dd',
        monthNames: [ "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월" ],
        monthNamesShort: [ "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월" ],
        dayNamesMin: [ "일", "월", "화", "수", "목", "금", "토" ],
        onSelect: function(dateText, inst) {
            getAvailiableTime(facilitiesId, dateText)
        }
    }).datepicker("setDate", new Date());
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

async function validationBooking(){

}

let bookedList = [];
const DEFAULT_HOUR_SELECT="시간"
async function getAvailiableTime(id, targetDate){
    const TYPE = 'FACILITY'
    await useAxios.get(`/api/v1/bookings/bookingType/${TYPE}/target/${id}/available-times`,
        {targetDate: targetDate}
        ,(res)=> {
            // 초기화
            document.querySelector('#hourSelect1.select .select-btn .select-value').innerHTML = DEFAULT_HOUR_SELECT;
            bookedList = [];
            console.log('res',res.data)
            if(res.data){
                bookedList = res.data;
                const hourOptionData = []
                for (let hour = 9; hour <= 23; hour++) {
                    hourOptionData.push({
                        'value' : hour,
                        'text' : hour,
                        'full' : bookedList.includes(hour)
                    });
                }
                var template = document.getElementById("select-option-template").innerHTML;
                var result = Mustache.render(template, {options: hourOptionData});
                document.getElementById("hourSelectOption1").innerHTML = result;
                document.getElementById("hourSelectOption2").innerHTML = result;
                const selectAll = document.querySelectorAll('.select .option-box .option.disabled');
                selectAll.forEach((all)=>{
                    all.addEventListener('click',()=>{
                        alert('hiroo');
                        document.querySelector('#hourSelect1.select .select-btn .select-value').addClass("level");
                    })
                });
            }
        },(err)=> {
            console.log('err',err)
        })
}
function initElementSetting(){
    // 옵션 추가: 00부터 23까지
    const hourOptionData = []
    for (let hour = 9; hour <= 23; hour++) {
        hourOptionData.push({
            'value' : hour,
            'text' : hour,
            'full' : true
        });
    }
    var template = document.getElementById("select-option-template").innerHTML;
    var result = Mustache.render(template, {options: hourOptionData});
    document.getElementById("hourSelectOption1").innerHTML = result;
    document.getElementById("hourSelectOption2").innerHTML = result;
}