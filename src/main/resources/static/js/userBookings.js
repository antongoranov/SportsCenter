// let btn = $('#showBookings');

let btn = document.getElementById('showBookings');
btn.addEventListener('click', showBookings);

// btn.click(() => {
//     showBookings(event)
// });

function showBookings() {
    // console.log('clicking successful')
    let bookingsContainer = document.getElementById('bookings-container');

    let btnText = btn.textContent;

    if (btnText === 'Show my bookings') {
        fetch("http://localhost:8080/api/myBookings")
            .then(res => res.json())
            .then(json => json.forEach(b => {

                //<li>
                let element = document.createElement('li');

                if (b.status === 'ACTIVE') {
                    //<a>
                    element = document.createElement('a');
                    element.href = "http://localhost:8080/myBookings"
                }


                element.textContent = `${b.sportClass.sportName} with ${b.sportClass.instructorName} 
                
                ${b.sportClass.dayOfWeek.charAt(0) + b.sportClass.dayOfWeek.slice(1).toLowerCase()} 
                
                from ${b.sportClass.startTime.slice(0, -3)} - ${b.sportClass.endTime.slice(0, -3)} 
                
                ${b.status}`;

                bookingsContainer.append(element);

                //$("#orders-container").appendChild(liElement);

            }))
            .catch(function (err) {
                console.log('Fetch error:', err)
            });

        btn.textContent = 'Hide bookings';

    } else if (btnText === 'Hide bookings') {

        bookingsContainer.textContent = '';
        btn.textContent = 'Show my bookings'
    }
}