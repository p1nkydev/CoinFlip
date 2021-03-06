package com.pinkydev.coinflip.flow.create

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.pinkydev.coinflip.*
import com.pinkydev.coinflip.flow.auth.user
import com.pinkydev.coinflip.flow.game.GameFragment
import com.pinkydev.common.event.CreateRoomEvent
import com.pinkydev.common.event.JoinToRoomEvent
import com.pinkydev.common.model.CreateRoomRequest
import com.pinkydev.common.model.Player
import com.pinkydev.common.model.JoinToRoomRequest
import kotlinx.android.synthetic.main.fragment_setup_game.*
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class SetupGameFragment : Fragment(R.layout.fragment_setup_game) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        socketFlow
            .filterNotNull()
            .onEach {

                if (activity?.supportFragmentManager?.findFragmentByTag("game") == null) {
                    activity
                        ?.supportFragmentManager
                        ?.beginTransaction()
                        ?.addToBackStack(null)
                        ?.replace(
                            R.id.container,
                            GameFragment.newInstance(sbMinimum.progress),
                            "game"
                        )
                        ?.commit()
                }
            }
            .launchIn(lifecycleScope)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            user = service.getUser()
            setupClicks()
        }
    }

    private fun setupClicks() {
        sbMinimum.max = user.balance
        sbMinimum.progress = user.balance / 10
        tvMinimumAmount.text = sbMinimum.progress.toString()
        tvBalance.text = user.balance.toString()

        sbMinimum.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvMinimumAmount.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        sideAccent.setOnClickListener {
            it.alpha = 1f
            sidePrimary.alpha = 0.2f
        }

        sidePrimary.setOnClickListener {
            it.alpha = 1f
            sideAccent.alpha = 0.2f
        }
        btnStartGame.setOnClickListener {
            socket.send(
                CreateRoomEvent(
                    CreateRoomRequest(
                        Player(id = user.id, name = user.name, getJoinedSide()),
                        sbMinimum.progress,
                    )
                )
            )
        }
    }

    // 0 - accent, 1 - primary
    private fun getJoinedSide(): Int = if (sideAccent.alpha == 1f) 0 else 1

}