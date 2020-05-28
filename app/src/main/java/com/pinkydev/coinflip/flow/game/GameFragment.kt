package com.pinkydev.coinflip.flow.game

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.pinkydev.coinflip.R
import com.pinkydev.coinflip.flow.auth.user
import com.pinkydev.coinflip.service
import com.pinkydev.coinflip.socketFlow
import com.pinkydev.common.event.PlayerJoinedEvent
import com.pinkydev.common.event.RoomWinnerEvent
import com.pinkydev.common.model.Player
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class GameFragment : Fragment(R.layout.fragment_game) {

    companion object {

        private const val MONEY_KEY = "sk"

        fun newInstance(moneyAmount: Int) = GameFragment().apply {
            arguments = bundleOf(MONEY_KEY to moneyAmount)
        }
    }

    private val moneyAmount by lazy { arguments?.getInt(MONEY_KEY) ?: 0 }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tvBalance.text = user.balance.toString()
        tvRoomMoney.text = "$$moneyAmount"
        ivCancelGame.setOnClickListener {
            lifecycleScope.launch {
                service.cancel(user.id)
                navigateOnStart()
            }
        }

        btnOk.setOnClickListener {
            navigateOnStart()
        }

        socketFlow
            .filterIsInstance<PlayerJoinedEvent>()
            .onEach { setupSide(it.player) }
            .launchIn(lifecycleScope)

        socketFlow
            .filterIsInstance<RoomWinnerEvent>()
            .onEach {
                ivCancelGame.visibility = View.GONE
                tvCountDown.visibility = View.VISIBLE
                tvRoomMoney.text = "$${it.moneyAmount}"

                setupSide(it.looser)
                setupSide(it.winner)

                (3 downTo 0).forEach {
                    delay(1000)
                    tvCountDown.text = it.toString()
                }

                flipView.flip(it.spins, it.time)

                tvCountDown.visibility = View.GONE
                delay(it.time)

                tvBalance.text = if (it.winner.id == user.id) {
                    (user.balance + moneyAmount).toString()
                } else {
                    (user.balance - moneyAmount).toString()
                }

                flipView.zoomIn()
                btnOk.isEnabled = true
            }
            .launchIn(lifecycleScope)
    }

    private fun navigateOnStart() {
        activity?.supportFragmentManager?.popBackStack()
    }

    private fun setupSide(player: Player) {
        if (player.joinedSide == 0) {
            tvShortLeft.text = player.name[0].toString()
            tvLeftPlayerName.text = player.name
            pbLeft.isVisible = false
            cardLeft.isVisible = true
        } else {
            tvShortRight.text = player.name[0].toString()
            tvRightPlayerName.text = player.name
            pbRight.isVisible = false
            cardRight.isVisible = true
        }
    }

}
