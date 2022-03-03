package com.example.smarttourapp.model;




        import java.util.List;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;


public class PricePredictionFlight {

    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("person")
    @Expose
    private String person;
    @SerializedName("totalCost")
    @Expose
    private Integer totalCost;
    @SerializedName("livingCost")
    @Expose
    private Integer livingCost;
    @SerializedName("hotelsAvgCost")
    @Expose
    private Integer hotelsAvgCost;
    @SerializedName("hotels")
    @Expose
    private List<Hotel> hotels = null;
    @SerializedName("flightAvgCost")
    @Expose
    private Integer flightAvgCost;
    @SerializedName("flight")
    @Expose
    private List<Flight> flight = null;





    public PricePredictionFlight(String day, String person, Integer totalCost, Integer livingCost, Integer hotelsAvgCost, List<Hotel> hotels, Integer flightAvgCost, List<Flight> flight) {
        super();
        this.day = day;
        this.person = person;
        this.totalCost = totalCost;
        this.livingCost = livingCost;
        this.hotelsAvgCost = hotelsAvgCost;
        this.hotels = hotels;
        this.flightAvgCost = flightAvgCost;
        this.flight = flight;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public Integer getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Integer totalCost) {
        this.totalCost = totalCost;
    }

    public Integer getLivingCost() {
        return livingCost;
    }

    public void setLivingCost(Integer livingCost) {
        this.livingCost = livingCost;
    }

    public Integer getHotelsAvgCost() {
        return hotelsAvgCost;
    }

    public void setHotelsAvgCost(Integer hotelsAvgCost) {
        this.hotelsAvgCost = hotelsAvgCost;
    }

    public List<Hotel> getHotels() {
        return hotels;
    }

    public void setHotels(List<Hotel> hotels) {
        this.hotels = hotels;
    }

    public Integer getFlightAvgCost() {
        return flightAvgCost;
    }

    public void setFlightAvgCost(Integer flightAvgCost) {
        this.flightAvgCost = flightAvgCost;
    }

    public List<Flight> getFlight() {
        return flight;
    }

    public void setFlight(List<Flight> flight) {
        this.flight = flight;
    }
    public class Flight {

        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("img")
        @Expose
        private String img;
        @SerializedName("cost")
        @Expose
        private Integer cost;
        @SerializedName("time")
        @Expose
        private String time;

        /**
         * No args constructor for use in serialization
         *
         */
        public Flight() {
        }

        /**
         *
         * @param img
         * @param cost
         * @param time
         * @param title
         */
        public Flight(String title, String img, Integer cost, String time) {
            super();
            this.title = title;
            this.img = img;
            this.cost = cost;
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public Integer getCost() {
            return cost;
        }

        public void setCost(Integer cost) {
            this.cost = cost;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

    }

    public class Hotel {

        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("img")
        @Expose
        private String img;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("info")
        @Expose
        private String info;
        @SerializedName("star")
        @Expose
        private String star;
        @SerializedName("price")
        @Expose
        private Integer price;
        @SerializedName("rating")
        @Expose
        private Float rating;
        @SerializedName("reviews")
        @Expose
        private String reviews;
        @SerializedName("booknow")
        @Expose
        private String booknow;

        /**
         * No args constructor for use in serialization
         *
         */
        public Hotel() {
        }

        /**
         *
         * @param img
         * @param star
         * @param reviews
         * @param price
         * @param booknow
         * @param rating
         * @param location
         * @param title
         * @param info
         */
        public Hotel(String title, String img, String location, String info, String star, Integer price, Float rating, String reviews, String booknow) {
            super();
            this.title = title;
            this.img = img;
            this.location = location;
            this.info = info;
            this.star = star;
            this.price = price;
            this.rating = rating;
            this.reviews = reviews;
            this.booknow = booknow;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public Float getRating() {
            return rating;
        }

        public void setRating(Float rating) {
            this.rating = rating;
        }

        public String getReviews() {
            return reviews;
        }

        public void setReviews(String reviews) {
            this.reviews = reviews;
        }

        public String getBooknow() {
            return booknow;
        }

        public void setBooknow(String booknow) {
            this.booknow = booknow;
        }

    }
}

