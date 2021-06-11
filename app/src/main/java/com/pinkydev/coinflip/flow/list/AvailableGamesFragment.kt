package com.pinkydev.coinflip.flow.list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pinkydev.coinflip.R
import com.pinkydev.coinflip.flow.auth.user
import com.pinkydev.coinflip.send
import com.pinkydev.coinflip.socket
import com.pinkydev.coinflip.socketFlow
import com.pinkydev.common.event.AvailableRoomsEvent
import com.pinkydev.common.event.GetRoomsEvent
import com.pinkydev.common.event.JoinToRoomEvent
import com.pinkydev.common.event.SocketEvent
import com.pinkydev.common.model.JoinToRoomRequest
import com.pinkydev.common.model.Player
import com.pinkydev.common.model.Room
import kotlinx.android.synthetic.main.fragment_available_games.*
import kotlinx.android.synthetic.main.item_room.view.*
import kotlinx.coroutines.flow.*

class AvailableGamesFragment : Fragment(R.layout.fragment_available_games) {

    private val adapter by lazy {
        GamesAdapter {
            socket.send(
                JoinToRoomEvent(
                    JoinToRoomRequest(
                        Player(
                            user.id, user.name,
                            if (it.players.first().joinedSide == 0) 1 else 0
                        ),
                        it
                    )
                )
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        availableGames.layoutManager = LinearLayoutManager(view.context)
        availableGames.adapter = adapter

        socketFlow
            .filterNotNull()
            .filterIsInstance<AvailableRoomsEvent>()
            .map { it.rooms }
            .onEach { adapter.setRooms(it) }
            .launchIn(lifecycleScope)

        socket.send(GetRoomsEvent())
    }
}


class GamesAdapter(private val onClick: (Room) -> Unit) :
    RecyclerView.Adapter<GamesAdapter.GameHolder>() {

    private var items = mutableListOf<Room>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameHolder {
        return GameHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GameHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setRooms(list: List<Room>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class GameHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(room: Room) {
            val you = room.players.first()
            with(view) {
                price.text = '₴' + room.moneyAmount.toString()
                userName.text = you.name
                // other joined as accent
                val color = if (you.joinedSide == 0) R.color.colorPrimary else R.color.colorAccent
                join.setBackgroundColor(ContextCompat.getColor(view.context, color))
                join.setOnClickListener {
                    onClick(room)
                }
            }
        }

    }
}

