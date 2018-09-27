package doggallery.mart.com.doggallery

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.pic_entry.view.*
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    var adapter: DogAdapter? = null
    var dogsList = ArrayList<Dog>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //set a separate large ImageView for the zoom in function
        largeImage.bringToFront()
        largeImage.setVisibility(View.INVISIBLE)
        largeImage.setOnClickListener()
        {
            largeImage.setVisibility(View.INVISIBLE)
        }
        //Create a list of dog pictures url
        CreateDogList()

        //make elements for the grid and set it
        adapter = DogAdapter(this, dogsList, largeImage)
        gvDogs.adapter = adapter

    }

    override fun onBackPressed() {
        if (largeImage.visibility == 0)
            largeImage.setVisibility(View.INVISIBLE)
        else
            finish()
    }

    class DogAdapter : BaseAdapter {
        var dogsList = ArrayList<Dog>()
        var context: Context? = null
        var largeImage: ImageView

        constructor(context: Context, dogsList: ArrayList<Dog>, largeImage: ImageView) : super() {
            this.context = context
            this.dogsList = dogsList
            this.largeImage = largeImage
        }

        override fun getCount(): Int {
            return dogsList.size
        }

        override fun getItem(position: Int): Any {
            return dogsList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val dog = this.dogsList[position]

            var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var dogView = inflator.inflate(R.layout.pic_entry, null)
            Picasso.get().load(dog.url).into(dogView.imgDog)

            dogView.imgDog.setOnClickListener()
            {
                largeImage.setImageDrawable(dogView.imgDog.drawable)
                largeImage.setVisibility(View.VISIBLE)
            }

            return dogView
        }
    }

    fun CreateDogList()
    {
        val fileText: String = applicationContext.assets.open("dog_urls.json").bufferedReader().use {
            it.readText()
        }

        val a: List<String> = fileText.split("[", ",", "]")
        for(i in 1..a.size-2)
        {
            val b: List<String> = a[i].split("\"")
            dogsList.add(Dog(b[1]))
        }
    }
}
